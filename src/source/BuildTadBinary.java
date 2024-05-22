package source;

import java.util.Stack;

import position.Position;
import tadexception.BoundaryViolationException;
import tadexception.InvalidPositionException;

public class BuildTadBinary {

	public static boolean Operator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/';
	}
	
	public static LinkedBinaryTree<String> buildExpression(String expression)
			throws InvalidPositionException, NonEmptyTreeException, EmptyTreeException, BoundaryViolationException {
		Stack<LinkedBinaryTree<String>> stack = new Stack<>();
		int n = expression.length();

		for (int i = 0; i < n; i++) {
			char ei = expression.charAt(i);

			// Se ei é uma variável (número) ou operador
			if (Character.isLetterOrDigit(ei) || Operator(ei)) {
				LinkedBinaryTree<String> T = new LinkedBinaryTree<>();
				T.addRoot(String.valueOf(ei));
				stack.push(T);
			} else if (ei == '(') {
				continue; // Ignorar o parêntese de abertura
			} else if (ei == ')') {
				// Combinar as últimas três árvores
				LinkedBinaryTree<String> T2 = stack.pop(); // E2
				LinkedBinaryTree<String> T = stack.pop(); // Operador
				LinkedBinaryTree<String> T1 = stack.pop(); // E1

				// Anexar E1 e E2 como subárvores do operador
				T.attach(T.root(), T1, T2);
				stack.push(T);
			}
		}
		return stack.pop(); // A árvore final será o único item restante na pilha
	}
	
	 // Método para percorrer a árvore em pós-ordem binário
    public static void binaryPostorder(LinkedBinaryTree<String> tree, Position<String> v) throws InvalidPositionException, BoundaryViolationException {
        if (tree.hasLeft(v)) {
            binaryPostorder(tree, tree.left(v));
        }
        if (tree.hasRight(v)) {
            binaryPostorder(tree, tree.right(v));
        }
        System.out.print(v.element());
    }

    
    public static double evaluateExpression(LinkedBinaryTree<String> tree, Position<String> v) throws InvalidPositionException, BoundaryViolationException {
        if (tree.isInternal(v)) {
            // Se o nodo é interno, avalie as subárvores esquerda e direita
            String operator = v.element();
            double x = evaluateExpression(tree, tree.left(v));
            double y = evaluateExpression(tree, tree.right(v));

            // Retorne o resultado da aplicação do operador
            switch (operator) {
                case "+":
                    return x + y;
                case "-":
                    return x - y;
                case "*":
                    return x * y;
                case "/":
                    return x / y;
                default:
                    throw new IllegalArgumentException("Operador desconhecido: " + operator);
            }
        } else {
            // Se o nodo é uma folha, retorne o valor armazenado nele
            return Double.parseDouble(v.element());
        }
    }
    
    public static void inorder(LinkedBinaryTree<String> tree, Position<String> v) throws InvalidPositionException, BoundaryViolationException {
        if (tree.hasLeft(v)) {
            inorder(tree, tree.left(v)); // percorre recursivamente a subárvore esquerda
        }

        System.out.print(v.element()); // execute a ação “de visita” sobre o nodo v

        if (tree.hasRight(v)) {
            inorder(tree, tree.right(v)); // percorre recursivamente a subárvore direita
        }
    }

}
