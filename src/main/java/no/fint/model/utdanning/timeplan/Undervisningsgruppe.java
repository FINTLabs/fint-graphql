// Built from tag release-3.2

package no.fint.model.utdanning.timeplan;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.NonNull;
import java.util.List;
import no.fint.model.FintMainObject;
import no.fint.model.utdanning.basisklasser.Gruppe;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class Undervisningsgruppe extends Gruppe implements FintMainObject {
    public enum Relasjonsnavn {
            FAG,
            SKOLE,
            ELEVFORHOLD,
            UNDERVISNINGSFORHOLD,
            TIME,
            GREPREFERANSE,
            VIGOREFERANSE,
            MEDLEMSKAP
    }

}
