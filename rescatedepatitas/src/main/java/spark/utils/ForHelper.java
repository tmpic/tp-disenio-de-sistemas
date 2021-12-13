package spark.utils;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public enum ForHelper implements Helper<Integer> {

    forHB{
        @Override
        public List<CharSequence> apply(Integer from, Options options) throws IOException {
            Integer to = options.param(0);
            Integer increment = options.param(1);
            List<CharSequence> acumm = new ArrayList<>();
            for (int i = from; i <= to; i += increment){
                acumm.add(options.fn(i));
            }
            return acumm;
        }
    }

}
