.PHONY: all
all: emfviews-plugin-dependencies.svg eview.png

emfviews-plugin-dependencies.svg: emfviews-plugin-dependencies.dot
	dot -Tsvg $< > $@

eview.png: eview.iuml
	java -jar ~/tools/plantuml.jar $<
