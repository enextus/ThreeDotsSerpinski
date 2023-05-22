package org.ThreeDotsSierpinski;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.sun.jna.ptr.IntByReference;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class RndNumberGeneratorTest {
    private RndNumberGenerator.iQuantumRandomNumberGenerator qrngMock;

    @BeforeEach
    void setUp() {
        qrngMock = Mockito.mock(RndNumberGenerator.iQuantumRandomNumberGenerator.class);
    }

    @Test
    void getIntegerArray_success() {
        int[] intArray = new int[RndNumberGenerator.INT_AMOUNT];
        IntByReference actualIntsReceived = new IntByReference(RndNumberGenerator.INT_AMOUNT);

        when(qrngMock.qrng_get_int_array(any(), eq(intArray.length), any()))
                .thenAnswer(invocation -> {
                    int[] argArray = invocation.getArgument(0);
                    IntByReference argIntsReceived = invocation.getArgument(2);
                    for (int i = 0; i < argArray.length; i++) {
                        argArray[i] = i; // Or any other logic to generate your random integers
                    }
                    argIntsReceived.setValue(argArray.length);
                    return 0;
                });

        RndNumberGenerator.getIntegerArray(qrngMock);
    }

    @Test
    void getIntegerArray_success2() {
        int[] intArray = new int[RndNumberGenerator.INT_AMOUNT];
        IntByReference actualIntsReceived = new IntByReference();

        when(qrngMock.qrng_get_int_array(intArray, intArray.length, actualIntsReceived)).thenReturn(0);

        RndNumberGenerator.getIntegerArray(qrngMock);
    }

    @Test
    void getAndPrintIntegerArray_failure() {
        int[] intArray = new int[RndNumberGenerator.INT_AMOUNT];
        IntByReference actualIntsReceived = new IntByReference();

        when(qrngMock.qrng_get_int_array(intArray, intArray.length, actualIntsReceived)).thenReturn(-1);

        RndNumberGenerator.getIntegerArray(qrngMock);
    }

}