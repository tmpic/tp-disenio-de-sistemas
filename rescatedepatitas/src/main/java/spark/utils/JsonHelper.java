package spark.utils;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.google.gson.Gson;

import java.io.IOException;

public enum JsonHelper implements Helper<Object> {
    json{
        @Override
        public CharSequence apply(Object arg0, Options arg1) throws IOException {
            Gson gson = new Gson();
            String jsonInString = gson.toJson(arg0);
            return arg1.fn(jsonInString);
        }
    }
}
