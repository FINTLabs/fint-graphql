// Built from tag release-3.2

package no.fint.model.resource.administrasjon.kompleksedatatyper;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import no.fint.model.FintComplexDatatypeObject;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class KontostrengResource implements FintComplexDatatypeObject, FintLinks {

    // Relations
    @Getter
    private final Map<String, List<Link>> links = createLinks();
        
    @JsonIgnore
    public List<Link> getAnsvar() {
        return getLinks().getOrDefault("ansvar", Collections.emptyList()); 
    }
    public void addAnsvar(Link link) {
        addLink("ansvar", link);
    }
    @JsonIgnore
    public List<Link> getArt() {
        return getLinks().getOrDefault("art", Collections.emptyList()); 
    }
    public void addArt(Link link) {
        addLink("art", link);
    }
    @JsonIgnore
    public List<Link> getFunksjon() {
        return getLinks().getOrDefault("funksjon", Collections.emptyList()); 
    }
    public void addFunksjon(Link link) {
        addLink("funksjon", link);
    }
    @JsonIgnore
    public List<Link> getProsjekt() {
        return getLinks().getOrDefault("prosjekt", Collections.emptyList()); 
    }
    public void addProsjekt(Link link) {
        addLink("prosjekt", link);
    }
}
