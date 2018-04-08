package ru.jihor.rat.processors;

import com.google.auto.service.AutoService;
import ru.jihor.rat.annotations.Remove;
import ru.jihor.rat.annotations.RemoveAfterDate;
import ru.jihor.rat.annotations.RemoveInVersion;
import ru.jihor.rat.utils.Pair;
import ru.jihor.rat.utils.Version;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Completion;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author jihor (jihor@ya.ru)
 * Created on 2018-04-06
 */

@AutoService(Processor.class)
public class RemovalAdviceProcessor extends AbstractProcessor {

    private static final Class<Remove> REMOVE = Remove.class;
    private static final Class<RemoveInVersion> REMOVE_IN_VERSION = RemoveInVersion.class;
    private static final Class<RemoveAfterDate> REMOVE_AFTER_DATE = RemoveAfterDate.class;
    private static final String RAT_PROJECT_VERSION = "rat.project.version";

    private Messager messager;
    private Version projectVersion;

    private static final LocalDate TODAY = LocalDate.now();

    private final String[] supportedAnnotationTypes = {REMOVE.getCanonicalName(),
            REMOVE_IN_VERSION.getCanonicalName(),
            REMOVE_AFTER_DATE.getCanonicalName()};

    private final String[] supportedOptions = {RAT_PROJECT_VERSION};

    @Override
    public Set<String> getSupportedOptions() {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(supportedOptions)));
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(supportedAnnotationTypes)));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.projectVersion = Version.from(processingEnv.getOptions().get(RAT_PROJECT_VERSION));
        messager.printMessage(Diagnostic.Kind.NOTE, "Project version to validate @RemoveInVersion against is: " + projectVersion);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        getStreamBase(roundEnv, REMOVE)
                .forEach(p -> messager.printMessage(Diagnostic.Kind.WARNING, getMessageForRemove(p._2()) , p._1()));

        getStreamBase(roundEnv, REMOVE_IN_VERSION)
                .filter(p -> projectVersion.isGreaterOrEqual(Version.from(p._2().version())))
                .forEach(p -> messager.printMessage(Diagnostic.Kind.WARNING, getMessageForRemoveInVersion(p._2()) , p._1()));

        getStreamBase(roundEnv, REMOVE_AFTER_DATE)
                .filter(p -> LocalDate.parse(p._2().date()).compareTo(TODAY) < 0)
                .forEach(p -> messager.printMessage(Diagnostic.Kind.WARNING, getMessageForRemoveAfterDate(p._2()) , p._1()));

        return true; // denote annotations as claimed
    }

    private <A extends Annotation> Stream<? extends Pair<? extends Element, A>> getStreamBase(RoundEnvironment roundEnv, Class<A> annotationClass) {
        return roundEnv.getElementsAnnotatedWith(annotationClass).stream()
                       .map(e -> Pair.of(e, e.getAnnotation(annotationClass)));
    }

    private String getMessageForRemove(Remove remove){
        return "Element should be removed" + maybeAddReason(remove.reason());
    }

    private String getMessageForRemoveInVersion(RemoveInVersion removeInVersion){
        return "Element should be removed in version " + removeInVersion.version() + maybeAddReason(removeInVersion.reason());
    }

    private String getMessageForRemoveAfterDate(RemoveAfterDate removeAfterDate){
        return "Element should be removed after " + removeAfterDate.date() + maybeAddReason(removeAfterDate.reason());
    }

    private String maybeAddReason(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            return "";
        }
        return ", reason is: " + reason;
    }

    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
        return null;
    }

}
