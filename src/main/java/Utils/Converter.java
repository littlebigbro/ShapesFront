package Utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Shape;


import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Converter {

    public static String shapeToJSON(Shape shape) {
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        writer.append("[");
        try {
            mapper.writeValue(writer, shape);
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.append("]");
        return writer.toString();
    }

    public static List<Shape> jsonToShapes(String json) {
        List<Shape> shapes = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            shapes = Arrays.asList(mapper.readValue(json, Shape[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shapes;
    }

    public static String createMapping(String[] params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.length; i = i + 2) {
            sb.append(params[i]).append("=").append(params[i + 1]);
            if (params.length - 2 > i) {
                sb.append(";");
            }
        }
        return sb.toString();
    }
}
