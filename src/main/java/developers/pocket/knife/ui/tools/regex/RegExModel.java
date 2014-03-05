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
            sb.append(line + "\n");
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(line);
            int count = 0;
            while (matcher.find()) {
                count++;
                sb.append("\t" + count + ". match:");
                sb.append(line.substring(matcher.start(), matcher.end()) + "\n");
                int groupCount = matcher.groupCount();
                for (int i = 1; i <= groupCount; i++) {
                    String group = matcher.group(i);
                    sb.append("\t\t" + i + ". group:" + group+"\n");
                }
            }
            if(count == 0) {
                sb.append("\tNo match.\n");
            }
        }
        return sb.toString();
    }
}
