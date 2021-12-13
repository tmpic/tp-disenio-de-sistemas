package spark.utils;

import java.io.IOException;
import java.util.Objects;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public enum EqualsHelper implements Helper<Object>{

	equals{
		@Override
		public CharSequence apply(Object obj1, Options options) throws IOException {
			obj1 = obj1 != null ? obj1 : "";
			Object obj2 = options.param(0);
			return Objects.equals(obj1, obj2) ? options.fn() : options.inverse();
		}
	}

}