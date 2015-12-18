package hu.zforgo.gravity;

import javaslang.Tuple3;
import javaslang.collection.List;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ClosestPointsTest {

	private static ClosestPoints finder;

	private Path input;
	private Path output;

	public ClosestPointsTest(String in, String out) {
		input = Paths.get("input", in);
		output = Paths.get("output", out);
	}

	@BeforeClass
	public static void init() {
		finder = ClosestPoints.getInstance();
	}

	@Parameterized.Parameters
	public static Collection<Object[]> loadConfiguration() throws IOException, URISyntaxException {
		return Arrays.asList(new Object[][]{
				{"sample_input_100_100.tsv", "sample_output_100_100.txt"},
				{"sample_input_2_8.tsv", "sample_output_2_8.txt"},
				{"sample_input_3_1000.tsv", "sample_output_3_1000.txt"},
				{"sample_input_10_100.tsv", "sample_output_10_100.txt"},
				{"sample_input_100_100.tsv", "sample_output_100_100.txt"},
		});
	}

	@Test
	public void demo() throws IOException {
		System.out.println("Processing: " + input.toAbsolutePath());
		Tuple3<List<Double>, List<Double>, Double> result = finder.find(input);
		try (FileWriter w = new FileWriter(output.toFile())) {
			w.write(String.format("p1: %s\np2: %s\ndist: %f", result._1, result._2, result._3));

			System.out.println("Output saved: " + output.toAbsolutePath());
		}
	}
}
