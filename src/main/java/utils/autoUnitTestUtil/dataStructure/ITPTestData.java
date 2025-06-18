package utils.autoUnitTestUtil.dataStructure;

import java.util.ArrayList;
import java.util.List;

public class ITPTestData {
    List<ParamTestData> paramList = new ArrayList<ParamTestData>();

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    String returnType = "";

    public String getTestDataName() {
        return testDataName;
    }

    public void setTestDataName(String testDataName) {
        this.testDataName = testDataName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    String testDataName = "";
    String fileName = "";
    String functionName = "";

    public ITPTestData() {

    }
    public ITPTestData(List<ParamTestData> paramList) {
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

        return "{" +
                "testDataName='" + testDataName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", functionName='" + functionName + '\'' +
                ", returnType='" + returnType + "\'"+
                ", paramList=" + str +
                '}';
    }

    public String toJSONString() {
        String str = "";
        for (int i = 0; i < paramList.size(); i++) {
            ParamTestData p = paramList.get(i);
            str += "    \"" + p.getName() + "\": \"" + p.getValue() + "\"";
            if (i < paramList.size() - 1) {
                str += ",\n";
            }
            else
            {
                str += "\n";
            }
        }

        return "{\n" +
                "    \"testDataName\": \"" + testDataName + "\", \n" +
                "    \"fileName\": \"" + fileName + "\", \n" +
                "    \"functionName\": \"" + functionName + "\", \n" +
                "    \"returnType\": \"" + returnType + "\", \n"+
                str +
                '}';
    }
}
