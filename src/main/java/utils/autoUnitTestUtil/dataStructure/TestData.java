package utils.autoUnitTestUtil.dataStructure;

import java.util.ArrayList;
import java.util.List;

public class TestData {
    List<ParamTestData> paramList = new ArrayList<ParamTestData>();

    public TestData() {

    }
    public TestData(List<ParamTestData> paramList) {
        this.paramList = paramList;
    }
    public List<ParamTestData> getParamList() {
        return paramList;
    }
    public void setParamList(List<ParamTestData> paramList) {
        this.paramList = paramList;
    }
    public void addParam(String key, String type, String value) {
        paramList.add(new ParamTestData(key, type, value));
    }
    public void removeParam(String key) {
        paramList.remove(key);
    }

    @Override
    public String toString() {
        String str = "";
        for (ParamTestData p : paramList) {
            str += p.toString() + "\n";
        }

        return "TestData{" +
                "paramList=" + str +
                '}';
    }
}
