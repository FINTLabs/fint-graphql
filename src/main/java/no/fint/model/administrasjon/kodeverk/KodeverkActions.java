// Built from tag release-3.2

package no.fint.model.administrasjon.kodeverk;

import java.util.Arrays;
import java.util.List;

public enum KodeverkActions {
	
	GET_ANSVAR,
	GET_ALL_ANSVAR,
	UPDATE_ANSVAR,
	GET_ARBEIDSFORHOLDSTYPE,
	GET_ALL_ARBEIDSFORHOLDSTYPE,
	UPDATE_ARBEIDSFORHOLDSTYPE,
	GET_ART,
	GET_ALL_ART,
	UPDATE_ART,
	GET_FRAVARSGRUNN,
	GET_ALL_FRAVARSGRUNN,
	UPDATE_FRAVARSGRUNN,
	GET_FRAVARSTYPE,
	GET_ALL_FRAVARSTYPE,
	UPDATE_FRAVARSTYPE,
	GET_FUNKSJON,
	GET_ALL_FUNKSJON,
	UPDATE_FUNKSJON,
	GET_LONNSART,
	GET_ALL_LONNSART,
	UPDATE_LONNSART,
	GET_PERSONALRESSURSKATEGORI,
	GET_ALL_PERSONALRESSURSKATEGORI,
	UPDATE_PERSONALRESSURSKATEGORI,
	GET_PROSJEKT,
	GET_ALL_PROSJEKT,
	UPDATE_PROSJEKT,
	GET_STILLINGSKODE,
	GET_ALL_STILLINGSKODE,
	UPDATE_STILLINGSKODE,
	GET_UKETIMETALL,
	GET_ALL_UKETIMETALL,
	UPDATE_UKETIMETALL
	;


    /**
     * Gets a list of all enums as string
     *
     * @return A string list of all enums
     */
    public static List<String> getActions() {
        return Arrays.asList(
                Arrays.stream(KodeverkActions.class.getEnumConstants()).map(Enum::name).toArray(String[]::new)
        );
    }

}

