package roy.love.roykook.test;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;

public class GsonWriteExample {
    // 示例对象
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Student {
        public String studno;
        public String studname;
        public String studsex;
    }
    public static void writeJsonToFile(String filePath, Object object) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        try (Writer writer = new FileWriter(filePath)) {
            writer.write(json);
        }
    }

        public static <T> T readJsonFromFile(String filePath, Class<T> clazz) throws IOException {
            Gson gson = new Gson();
            try (Reader reader = new FileReader(filePath)) {
                return gson.fromJson(reader, clazz);
            }
        }
    public static void main(String[] args) throws IOException {
//        Student student = new Student();
//        student.studno = "11111";
//        student.studname = "wwww";
//        student.studsex = "男";
//        writeJsonToFile("student.json", student);
        Student student = readJsonFromFile("student.json", Student.class);
        System.out.println("学号: " + student.studno + ", 姓名: " + student.studname + ", 性别: " + student.studsex);
    }
}
