import java.io.*;
import java.util.*;

public class Unicode {
    static class Tuple<T1, T2> {
	final T1 left;
	final T2 right;

	public Tuple(final T1 _left, final T2 _right) {
	    this.left = _left;
	    this.right = _right;
	}
    }
    
    public static void main(String[] args) throws Exception {
	if (args.length < 1) {
	    System.err.println("Usage: java Unicode <UnicodeData.txt>");
	    System.exit(1);
	}

	FileReader fileReader = new FileReader(args[0]);
	BufferedReader reader = new BufferedReader(fileReader);

	HashMap<String, List<Tuple<String, String>>> categoryRanges = new HashMap<>();


	String line = reader.readLine();

	String[] init = line.split(";");
	String currentCategory = init[2];
	String startPoint = init[0];
	String endPoint = init[0];
	
	while ((line = reader.readLine()) != null) {
	    String[] splitLine = line.split(";");

	    String category = splitLine[2];
	    if (category.equals(currentCategory)) {
		endPoint = splitLine[0];
	    } else {
		List<Tuple<String, String>> lst = categoryRanges.get(currentCategory);

		if (lst == null) {
		    lst = new ArrayList<>();
		    categoryRanges.put(currentCategory, lst);
		}

		lst.add(new Tuple<>(startPoint, endPoint));

		currentCategory = category;
		startPoint = splitLine[0];
		endPoint = splitLine[0];
	    }
	}

	System.out.println("lexer grammar UnicodeLex;");
	System.out.println();
	
	for (Map.Entry<String, List<Tuple<String, String>>> entry : categoryRanges.entrySet()) {
	    if (entry.getValue().size() > 0) {
		System.out.println("Unicode_Cat_" + entry.getKey());
		System.out.println("\t: \'\\u" + entry.getValue().get(0).left + "\'..\'\\u" + entry.getValue().get(0).right + "\'");
		for (int idx=1; idx < entry.getValue().size(); ++idx) {
		    Tuple<String, String> tuple = entry.getValue().get(idx);

		    /* Antlr doesn't support codes > 4 */
		    if (tuple.left.length() > 4 || tuple.right.length() > 4) { // XXX: Needs fixed, could have left 4 and right 5
			continue;
		    }
		    
		    System.out.println("\t| \'\\u" + tuple.left + "\'..\'\\u" + tuple.right + "\'");
		}
		System.out.println("\t;");
		System.out.println();
	    }
	}
    }
}
