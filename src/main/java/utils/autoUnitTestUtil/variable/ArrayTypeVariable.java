package utils.autoUnitTestUtil.variable;

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Type;

public class ArrayTypeVariable extends Variable {
    private ArrayType type;

    public ArrayTypeVariable(ArrayType type, String name) {
        this.type = type;
        super.setName(name);
    }

    @Override
    public Type getType() {
        return type;
    }
}
