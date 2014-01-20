package developers.pocket.knife.i18n;

import jb5n.api.JB5n;

import javax.enterprise.inject.Produces;

public class MessagesProducer {

    @Produces
    public Messages produceMessages() {
        return JB5n.createInstance(Messages.class);
    }
}
