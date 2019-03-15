// Built from tag release-3.2

package no.fint.model.felles.kompleksedatatyper;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.NonNull;
import java.util.List;
import no.fint.model.FintComplexDatatypeObject;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Personnavn implements FintComplexDatatypeObject {
    @NonNull
    private String etternavn;
    @NonNull
    private String fornavn;
    private String mellomnavn;
}
