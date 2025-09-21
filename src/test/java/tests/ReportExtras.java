package tests;

import org.testng.IExecutionListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Generates test-output/screenshots.html with buttons linking to all PNGs in /screenshots.
 */
public class ReportExtras implements IExecutionListener {

    @Override
    public void onExecutionFinish() {
        try {
            String projectDir = System.getProperty("user.dir");
            Path screenshotsDir = Paths.get(projectDir, "screenshots");
            Path testOutputDir  = Paths.get(projectDir, "test-output");
            Files.createDirectories(testOutputDir);

            List<Path> pngs;
            if (Files.exists(screenshotsDir)) {
                // newest first
                try (Stream<Path> s = Files.list(screenshotsDir)) {
                    pngs = s.filter(p -> p.getFileName().toString().toLowerCase().endsWith(".png"))
                            .sorted(Comparator.comparingLong((Path p) -> p.toFile().lastModified()).reversed())
                            .collect(Collectors.toList());
                }
            } else {
                pngs = List.of();
            }

            StringBuilder body = new StringBuilder();
            body.append("<h1>Run Screenshots</h1>\n");
            if (pngs.isEmpty()) {
                body.append("<p>No screenshots found in <code>/screenshots</code> for this run.</p>");
            } else {
                body.append("<div class='grid'>\n");
                for (Path p : pngs) {
                    String rel = "../screenshots/" + p.getFileName();
                    String name = p.getFileName().toString();
                    body.append("<div class='card'>")
                            .append("<a class='btn' target='_blank' href='").append(rel).append("'>Open screenshot</a>")
                            .append("<div class='name'>").append(name).append("</div>")
                            .append("<a target='_blank' href='").append(rel).append("'>")
                            .append("<img src='").append(rel).append("' loading='lazy'/>")
                            .append("</a>")
                            .append("</div>\n");
                }
                body.append("</div>\n");
            }

            String html = """
                    <!doctype html>
                    <html lang="en">
                    <head>
                      <meta charset="utf-8">
                      <title>Screenshots</title>
                      <style>
                        body{font-family:Arial,Helvetica,sans-serif;margin:24px;}
                        .grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(260px,1fr));gap:16px;}
                        .card{border:1px solid #e5e7eb;border-radius:12px;padding:12px;box-shadow:0 1px 3px rgba(0,0,0,.06);}
                        .btn{display:inline-block;background:#1976d2;color:#fff;text-decoration:none;
                             padding:8px 12px;border-radius:8px;margin-bottom:8px}
                        .btn:hover{background:#145ea8;}
                        .name{font-size:12px;color:#555;margin:4px 0 8px}
                        img{max-width:100%;height:auto;border-radius:8px;border:1px solid #eee}
                        code{background:#f3f4f6;padding:2px 4px;border-radius:4px}
                        a{color:#1976d2}
                      </style>
                    </head>
                    <body>
                    """ + body + """
                    <p style="margin-top:24px">
                      Back to <a href="./index.html">TestNG index.html</a> or
                      <a href="./emailable-report.html">emailable-report.html</a>
                    </p>
                    </body>
                    </html>
                    """;

            Path out = testOutputDir.resolve("screenshots.html");
            Files.writeString(out, html, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("🧾 Created: " + out.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Failed to write screenshots.html: " + e.getMessage());
        }
    }
}
