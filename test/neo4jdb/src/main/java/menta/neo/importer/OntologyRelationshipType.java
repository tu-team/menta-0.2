package menta.neo.importer;

import org.neo4j.graphdb.RelationshipType;

/**
 * Ontology relation type for data model importing to Neo4j.
 *
 * @author ayratn
 */
public enum OntologyRelationshipType implements RelationshipType {
	CATEGORIZED_AS(null, null),  // pseudo-rel
	CONTAINS(null, null),        // pseudo-rel
	IS_INSTANCE_OF("type", "hasInstance"),
	SUBCLASS_OF("subClassOf", "superClassOf"),
	HAS_INSTANCE("hasInstance", "type"),
	SUPERCLASS_OF("superClassOf", "subClassOf");

	private String name;
	private String inverseName;

	OntologyRelationshipType(String name, String inverseName) {
		this.name = name;
		this.inverseName = inverseName;
	}

	public static OntologyRelationshipType fromName(String name) {
		for (OntologyRelationshipType type : values()) {
			if (name.equals(type.name)) {
				return type;
			}
		}
		return null;
	}

	public static OntologyRelationshipType inverseOf(String name) {
		OntologyRelationshipType rel = fromName(name);
		if (rel != null && rel.inverseName != null) {
			return fromName(rel.inverseName);
		} else {
			return null;
		}
	}
}