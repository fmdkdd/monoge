.PHONY: all
all: emfviews-plugin-dependencies.svg emfviews-class-diagram.svg emfviews-object-diagram.svg

%.svg: %.dot
	dot -Tsvg $< > $@

%.svg: %.iuml
	java -jar ~/tools/plantuml.jar -tsvg $<
