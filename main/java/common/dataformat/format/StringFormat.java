package common.dataformat.format;

import common.dataformat.Format;

public class StringFormat implements Format<String> {

    public String format(String object) throws Exception {
        return object;
    }

    public String parse(String string) throws Exception {
        return string;
    }

}
