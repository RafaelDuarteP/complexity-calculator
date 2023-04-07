package calculator;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;

public class NPathComplexityCalculator {

	public static int calculateNPathComplex(Node node) {
		int complex = 0;

		for (Node child : node.getChildNodes()) {
			if (child instanceof MethodDeclaration) {
				complex += calculateNPathComplexByMethod(child);
			} else {
				complex += calculateNPathComplex(child);
			}
		}

		return complex;
	}

	public static int calculateNPathComplexByMethod(Node node) {
		return (int) Math.pow(2, calculateN(node));
	}

	private static int calculateN(Node node) {
		int npath = 0;
		for (Node child : node.getChildNodes()) {
			if (child instanceof IfStmt || child instanceof SwitchStmt || child instanceof WhileStmt
					|| child instanceof ForStmt || child instanceof DoStmt || child instanceof ConditionalExpr) {
				npath += 2;
				npath += calculateN(child);
			} else {
				npath += calculateN(child);
			}
		}
		return npath;
	}
}
