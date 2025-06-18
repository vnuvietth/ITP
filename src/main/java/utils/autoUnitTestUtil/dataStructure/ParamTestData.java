package utils.autoUnitTestUtil.dataStructure;

public class ParamTestData {
    private String name;
    private String type;
    private Object value;
    public ParamTestData(String key, String type, Object value) {
        this.name = key;
        this.type = type;
        this.value = value;
    }
    public String getName() {
        return name;
    }
    public Object getValue() {
        return value;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ParamTestData{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", value=" + value +
                '}';
    }

    public String toJSONString() {
        return "{" +
                "\"name\"=\"" + name + "\", \n" +
                "\"type\"=\"" + type + "\", \n" +
                "\"value\"=\"" + value + "\"\n" +
                '}';
    }
}
