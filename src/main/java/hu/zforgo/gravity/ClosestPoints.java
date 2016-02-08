package hu.zforgo.gravity;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.collection.Iterator;
import javaslang.collection.List;
import javaslang.collection.Stream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.function.Function;

public class ClosestPoints {

	Function<String[], List<Double>> convert = (i) -> Stream.of(i).map(Double::parseDouble).toList();

	Function<Tuple2<List<Double>, List<Double>>, Tuple3<List<Double>, List<Double>, Double>> calculate = (i) -> Tuple.of(
			i._1,
			i._2,
			i._1.zip(i._2).map(t -> Math.pow(t._1 - t._2, 2)).sum().doubleValue()
	);

	public Tuple3<List<Double>, List<Double>, Double> find(Path input) throws IOException {
		return Files.lines(input).collect(List.collector())
				.map(s -> s.split("\t"))
				.map(convert::apply)
				.combinations(2)
				.toJavaStream().parallel() // back to java.util.Stream which is parallel
				.map(s -> Tuple.of(s.get(0), s.get(1)))
				.map(calculate::apply)
				.min((o1,o2)-> o1._3.compareTo(o2._3))
				.get()
				.transform((o1,o2,d)-> Tuple.of(o1,o2, Math.sqrt(d))); // Calculate sqrt only once
	}
}
