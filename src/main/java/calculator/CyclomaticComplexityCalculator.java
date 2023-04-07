package calculator;

import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithBody;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.WhileStmt;

public class CyclomaticComplexityCalculator {

	
	public static int calculateCyclomaticComplex(Node node) {
		int complex = 0;
		
		for (Node child : node.getChildNodes()) {
			if (child instanceof MethodDeclaration) {
				complex += calculatePredicateNodes(child) + 1;
			}else {
				complex += calculateCyclomaticComplex(child);
			}
		}
		
		return complex;
	}

	private static int calculatePredicateNodes(Node node) {
		int complexity = 0;
		for (Node child : node.getChildNodes()) {
			if (child instanceof IfStmt) {
				complexity += calculatePredicateNodes(((IfStmt) child).getThenStmt());
				Optional<Statement> elseStmt = ((IfStmt) child).getElseStmt();
				if (elseStmt.isPresent()) {
					complexity += calculatePredicateNodes(elseStmt.get());
				}
				complexity++;
			} else if (child instanceof ForStmt || child instanceof ForEachStmt || child instanceof WhileStmt
					|| child instanceof DoStmt) {
				complexity += calculatePredicateNodes(((NodeWithBody<?>) child).getBody());
				complexity++;
			} else if (child instanceof SwitchStmt) {
				SwitchStmt switchStmt = (SwitchStmt) child;
				int cases = switchStmt.getEntries().size();
				for (SwitchEntry entry : switchStmt.getEntries()) {
					if (entry.getStatements().size() > 0) {
						complexity += calculatePredicateNodes(entry.getStatements().get(0));
					}
				}
				complexity += cases;
			} else if (child instanceof ConditionalExpr) {
				complexity++;
			} else if (child instanceof TryStmt) {
				TryStmt tryStmt = (TryStmt) child;
				complexity += calculatePredicateNodes(tryStmt.getTryBlock());
				for (CatchClause catchClause : tryStmt.getCatchClauses()) {
					complexity += calculatePredicateNodes(catchClause.getBody());
				}
				Optional<BlockStmt> finallyBlock = tryStmt.getFinallyBlock();
				if (finallyBlock.isPresent()) {
					complexity += calculatePredicateNodes(finallyBlock.get());
				}
				complexity++;
			} else {
				complexity += calculatePredicateNodes(child);
			}
		}
		return complexity;
	}
}
