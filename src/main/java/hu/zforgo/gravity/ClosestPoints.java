package hu.zforgo.gravity;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.collection.Iterator;
import javaslang.collection.List;
import javaslang.collection.Stream;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.function.Function;

public class ClosestPoints {

	public static volatile ClosestPoints INSTANCE;
	private ClosestPoints(){}

	public static ClosestPoints getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ClosestPoints();
		}
		return INSTANCE;
	}

	private static List<String> readLines(Path path) {
		final Scanner scanner;
		try {
			scanner = new Scanner(path.toFile());
		} catch (FileNotFoundException e) {
			return List.empty();
		}
		return List.ofAll(new Iterator<String>() {
			@Override
			public boolean hasNext() {
				return scanner.hasNextLine();
			}

			@Override
			public String next() {
				return scanner.nextLine();
			}
		});
	}

	Function<String[], List<Double>> convert = (i) -> Stream.of(i).map(Double::parseDouble).toList();

	Function<Tuple2<List<Double>, List<Double>>, Tuple3<List<Double>, List<Double>, Double>> calculate = (i) -> Tuple.of(
			i._1,
			i._2,
			Math.sqrt(i._1.zip(i._2).map(t -> Math.pow(t._1 - t._2, 2)).sum().doubleValue())
	);

	public Tuple3<List<Double>, List<Double>, Double> find(Path input) {
		 return readLines(input)
				.map(s -> s.split("\t"))
				.map(convert::apply)
				.combinations(2)
				.map(s -> Tuple.of(s.get(0), s.get(1)))
				.map(calculate::apply)
				.sort((o1, o2) -> o1._3.compareTo(o2._3))
				.get(0);
	}
}
