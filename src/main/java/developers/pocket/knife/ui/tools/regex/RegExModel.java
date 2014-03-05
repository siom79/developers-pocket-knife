package developers.pocket.knife.ui.tools.regex;

import developers.pocket.knife.i18n.Messages;

import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExModel {
    @Inject
    Messages messages;

    public String applyRegEx(String regEx, String testData) {
        StringBuilder sb = new StringBuilder();
        String lines[] = testData.split("\\r?\\n");
        for (String line : lines) {
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(line);
            int count = 0;
            while (matcher.find()) {
                count++;
                if (count > 1) {
                    sb.append(" ");
                }
                sb.append(count + ". match:");
                sb.append(line.substring(matcher.start(), matcher.end()));
                int groupCount = matcher.groupCount();
                for (int i = 1; i <= groupCount; i++) {
                    String group = matcher.group(i);
                    if(i == 1 && count >= 1) {
                        sb.append(" ");
                    }
                    if (i > 1) {
                        sb.append(" ");
                    }
                    sb.append(i + ". group:" + group);
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
