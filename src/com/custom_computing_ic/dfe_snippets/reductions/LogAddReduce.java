package com.custom_computing_ic.dfe_snippets.reductions;
/***
    This kernel implements add-reduce circuit with logarithmic
    number of adders (wrt to the maximum number of values in the stream
    to be correctly reduced). Here we make it with ceil(log2(maxTermsToReduce)
    adders, e.g. 4 adders to reduce up to 16 values.

    This is not a general accumulator as it has no feedback loop.
    Indended use case: reduce partial sums produced by outer feedback loop.

    Note: this code does NOT correctly accumulate streams longer than
    maxTermsToReduce. For long enough streams it's cheaper in resources
    to accumulate given stream to partial sums and then reduce partial
    sums with this code.

    Caveat: outer code needs to count last maxTermsToReduce partial terms
    and only then to send inputEnable=1 signal to this curcuit. Why: here
    we have internal state.
*/
import com.maxeler.maxcompiler.v2.kernelcompiler.KernelLib;
import com.maxeler.maxcompiler.v2.kernelcompiler.types.base.DFEType;
import com.maxeler.maxcompiler.v2.kernelcompiler.types.base.DFEVar;
import com.maxeler.maxcompiler.v2.utils.MathUtils;

public class LogAddReduce extends KernelLib
{
    private DFEVar output;
    private DFEVar outputValid;

    public  DFEVar getOutput()       {   return output;        }
    public  DFEVar isOutputValid()   {   return outputValid;   }

    /***
        @ param     newTerm             new input term subject to be add-reduced
        @ param     inputEnable         boolean: indicating whether newTerm is really new input or it should be ignored
        @ param     stopSignal          boolean: forcing to complete reduction and output sum of previously fed values
        @ param     maxTermsToReduce    indicates maximum number of terms this instance will be reducing correctly
        @ param     dataType            underlying data type of add-reduced data
    */
    public LogAddReduce(KernelLib owner, DFEVar inputEnable, DFEVar stopSignal,
                                            DFEVar newTerm, DFEType dataType, int maxTermsToReduce)
    {
        super(owner);

        int numLayers = MathUtils.ceilLog2(maxTermsToReduce);

        DFEVar enable = inputEnable;
        DFEVar input  = newTerm;
        for (int i = 1; i <= numLayers; i++)
        {
            TwoStageStoreReduce thisLayer = new TwoStageStoreReduce(this, enable, stopSignal, input, dataType);
            enable = thisLayer.isOutputValid();
            input  = thisLayer.getOutput();

            if (i == numLayers)
            {
                output = thisLayer.getOutput();
                outputValid = thisLayer.isOutputValid();
            }
        }
    }
}
