// Built from tag release-3.2

package no.fint.model.administrasjon.personal;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.NonNull;
import java.util.List;
import no.fint.model.FintAbstractObject;
import java.util.Date;
import no.fint.model.administrasjon.kompleksedatatyper.Kontostreng;
import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.felles.kompleksedatatyper.Identifikator;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class Lonn implements FintAbstractObject {
    private Date anvist;
    private Date attestert;
    @NonNull
    private String beskrivelse;
    private Date kontert;
    @NonNull
    private Kontostreng kontostreng;
    private Periode opptjent;
    @NonNull
    private Periode periode;
    private Identifikator systemId;
}
