package utils.autoUnitTestUtil.dataStructure;

import java.util.ArrayList;
import java.util.List;

public class TestData {
    List<KeyValuePair> paramList = new ArrayList<KeyValuePair>();

    public TestData() {

    }
    public TestData(List<KeyValuePair> paramList) {
        this.paramList = paramList;
    }
    public List<KeyValuePair> getParamList() {
        return paramList;
    }
    public void setParamList(List<KeyValuePair> paramList) {
        this.paramList = paramList;
    }
    public void addParam(String key, String value) {
        paramList.add(new KeyValuePair(key, value));
    }
    public void removeParam(String key) {
        paramList.remove(key);
    }
}
