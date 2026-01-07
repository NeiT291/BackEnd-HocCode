package com.neit.hoccode.service;

import com.neit.hoccode.dto.response.RunResult;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class DockerCodeRunnerService {
    public RunResult run(String language, String code) {

        Path dir = null;
        String containerName = "job-" + UUID.randomUUID();

        long startTime = System.nanoTime();

        try {
            dir = Files.createTempDirectory("job-");

            Files.writeString(
                    dir.resolve(getFileName(language)),
                    code,
                    StandardCharsets.UTF_8
            );

            ProcessBuilder runPb = new ProcessBuilder(
                    "docker", "run",
                    "--name", containerName,
                    "--network", "none",
                    "--cpus=0.5",
                    "--memory=256m",
                    "-v", dir.toAbsolutePath() + ":/app",
                    getImageName(language)
            );

            Process runProcess = runPb.start();

            boolean finished = runProcess.waitFor(5, TimeUnit.SECONDS);
            if (!finished) {
                runProcess.destroyForcibly();
                return new RunResult(
                        "",
                        "Timeout",
                        124,
                        elapsedMs(startTime),
                        0
                );
            }

            String stdout = new String(runProcess.getInputStream().readAllBytes());
            String stderr = new String(runProcess.getErrorStream().readAllBytes());

            double memoryMb = readMemoryUsage(containerName);

            return new RunResult(
                    stdout,
                    stderr,
                    runProcess.exitValue(),
                    elapsedMs(startTime),
                    memoryMb
            );

        } catch (Exception e) {
            return new RunResult(
                    "",
                    e.getMessage(),
                    500,
                    elapsedMs(startTime),
                    0
            );
        } finally {
            cleanup(containerName, dir);
        }
    }

    private long elapsedMs(long startNano) {
        return (System.nanoTime() - startNano) / 1_000_000;
    }

    private double readMemoryUsage(String containerName) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "docker", "stats",
                    containerName,
                    "--no-stream",
                    "--format", "{{.MemUsage}}"
            );

            Process p = pb.start();
            String output = new String(p.getInputStream().readAllBytes()).trim();
            // ví dụ: "23.5MiB / 256MiB"
            return parseMemoryMb(output);
        } catch (Exception e) {
            return 0;
        }
    }

    private double parseMemoryMb(String mem) {
        try {
            String used = mem.split("/")[0].trim(); // "23.5MiB"
            if (used.endsWith("MiB")) {
                return Double.parseDouble(used.replace("MiB", "").trim());
            }
            if (used.endsWith("KiB")) {
                return Double.parseDouble(used.replace("KiB", "").trim()) / 1024;
            }
        } catch (Exception ignored) {}
        return 0;
    }

    private void cleanup(String containerName, Path dir) {
        try {
            new ProcessBuilder("docker", "rm", "-f", containerName).start();
        } catch (Exception ignored) {}

        if (dir != null) {
            try {
                Files.walk(dir)
                        .sorted(Comparator.reverseOrder())
                        .forEach(p -> p.toFile().delete());
            } catch (Exception ignored) {}
        }
    }

    // ==== mapping ====

    private String getImageName(String language) {
        return switch (language) {
            case "python" -> "runner-python";
            case "java" -> "runner-java";
            case "cpp" -> "runner-cpp";
            default -> throw new IllegalArgumentException("Unsupported language");
        };
    }

    private String getFileName(String language) {
        return switch (language) {
            case "python" -> "main.py";
            case "java" -> "Main.java";
            case "cpp" -> "main.cpp";
            default -> throw new IllegalArgumentException("Unsupported language");
        };
    }
}
