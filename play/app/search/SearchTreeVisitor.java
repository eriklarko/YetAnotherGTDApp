package search;

import java.util.Collection;
import removewhenjava8.Consumer;
import removewhenjava8.Predicate;

/**
 *
 * @author Thinner
 */
public class SearchTreeVisitor {


    public static void visit(Node searchTree, Consumer<Node> visitor) {
        doVisit(searchTree, new Predicate<Node>() {

            @Override
            public boolean test(Node t) {
                return true;
            }
        }, visitor);
    }

    public static void visit(Node searchTree, Predicate<Node> shouldVisit, Consumer<Node> visitor) {
        doVisit(searchTree, shouldVisit, visitor);
    }

    private static void doVisit(Node searchTree, Predicate<Node> shouldVisit, Consumer<Node> visitor) {

        if (!shouldVisit.test(searchTree)) {
            return;
        }

        if (searchTree instanceof And) {
             visitAnd((And) searchTree, shouldVisit, visitor);
        }
        if (searchTree instanceof Or) {
             visitOr((Or) searchTree, shouldVisit, visitor);
        }
        if (searchTree instanceof Not) {
             visitNot((Not) searchTree, shouldVisit, visitor);
        }
        if (searchTree instanceof IdEq) {
             visitIdEq((IdEq) searchTree, visitor);
        }

        throw new IllegalArgumentException("Unknown node type");
    }

    private static void visitAnd(And and, Predicate<Node> shouldVisit, Consumer<Node> visitor) {
        visitor.accept(and);
        visitChildren(and.getChildren(), shouldVisit, visitor);
    }

    private static void visitChildren(Collection<Node> children, Predicate<Node> shouldVisit, Consumer<Node> visitor) {
        for (Node child : children) {
            doVisit(child, shouldVisit, visitor);
        }
    }

    private static void visitOr(Or or, Predicate<Node> shouldVisit, Consumer<Node> visitor) {
        visitor.accept(or);
        visitChildren(or.getChildren(), shouldVisit, visitor);
    }

    private static void visitNot(Not not, Predicate<Node> shouldVisit, Consumer<Node> visitor) {
        visitor.accept(not);
        doVisit(not.getChild(), shouldVisit, visitor);
    }

    private static void visitIdEq(IdEq ideq, Consumer<Node> visitor) {
        visitor.accept(ideq);
    }
}
