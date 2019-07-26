package it.is.all.good;

import java.util.function.BiFunction;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//Can replace Template pattern, avoid inheritance
public class TypeSpecificFunctionality {

    public static void main(String[] args) {
        ExternalDependency dependency = mock(ExternalDependency.class);
        when(dependency.getResource()).thenReturn(2);

        TypeSpecificService service = new TypeSpecificService(dependency);
        System.out.println("A_TYPE : " + service.handle(Type.A_TYPE, 2));
        System.out.println("B_TYPE : " + service.handle(Type.B_TYPE, 2));
        System.out.println("C_TYPE : " + service.handle(Type.C_TYPE, 2));
    }

    enum Type {
        A_TYPE(TypeSpecificService::handleTypeB),
        B_TYPE(TypeSpecificService::handleTypeA),
        C_TYPE(TypeSpecificService::handleTypeC);
        public final BiFunction<TypeSpecificService, Integer, Integer> handler;

        Type(BiFunction<TypeSpecificService, Integer, Integer> handler) {
            this.handler = handler;
        }

    }

    interface ExternalDependency {
        int getResource();
    }

    static class TypeSpecificService {
        private final ExternalDependency dependency;

        TypeSpecificService(ExternalDependency dependency) {
            this.dependency = dependency;
        }

        int handleTypeA(int integer) {
            return integer * dependency.getResource();
        }

        int handleTypeB(int integer) {
            return integer + 1;
        }

        int handleTypeC(int integer) {
            return 5;
        }

        int handle(Type type, int days) {
            return type.handler.apply(this, days);
        }
    }
}
