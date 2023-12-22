package csie.bdsa.lms.shared.util;

import java.io.Serializable;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

public class CustomIdentityGenerator extends IdentityGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        Serializable id = session.getEntityPersister(null, object)
                .getClassMetadata()
                .getIdentifier(object, session);
        return id != null ? id : super.generate(session, object);
    }
}
