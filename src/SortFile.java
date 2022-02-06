import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class SortFile {

	public static void main(String[] args) {
		System.out.println("start");

		if (args.length != 1) {
			Arrays.asList(args).forEach(a -> System.out.println(a.toString()));
			throw new RuntimeException("I need dir name.");
		}

		File originDirFile = new File(args[0]);
		if (!originDirFile.isDirectory()) {
			throw new RuntimeException("I need dir name.");
		}

		Arrays.asList(originDirFile.listFiles()).forEach(f -> {
			System.out.println(f);

			if (f.isDirectory()) {
				System.out.println("This is directory.");
				return;
			}

			try {
				Path filePath = Path.of(f.getPath());
				Instant lastModifiedInstant = Files.getLastModifiedTime(filePath).toInstant();
				LocalDateTime localDateTime = LocalDateTime.ofInstant(lastModifiedInstant, ZoneId.systemDefault());
				String dirName = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
				File dir = new File(originDirFile.getPath(), dirName);

				if (dir.exists()) {
					System.out.println("Exists : " + dir.getAbsolutePath());
				} else {
					dir.mkdir();
					System.out.println("Make dir : " + dir.getAbsolutePath());
				}

				Path dirPath = Path.of(dir.getPath(), f.getName());
				Files.move(filePath, dirPath);

			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		System.out.println("end");
	}

}
