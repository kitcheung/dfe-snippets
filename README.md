# dfe-snippets

`dfe-snippets` is a collection of building blocks for projects using Maxeler Dataflow Engines (DFEs).
It contains useful code both for the CPU side (C++) and the DFE side (MaxJ).

## Requires

1. MaxCompiler 2013+
2. Compatible [Maxeler](http://www.maxeler.com/products/) hardware
3. python 2.7 (for running scripts)

## Structure

Implementations are grouped by concept (e.g. Summation, Infrastructure, Sorting) as shown below:

```
scripts/          ---> Useful scripts
concept1/
  implementation1/
     src/          ---> source code
     build         ---> build directory
```

## Using

### DFE Libraries

To use the DFE libraries you only need to add the src directory to your classpath when running `maxjc`. For example:
`maxjc -cp <path/to/snippets>/src/:<other/class/paths> ....`

If you use the Makefile.common provided with `dfe-snippets` you can just add this to the `JFLAGS` variable. For example:
`JFLAGS=-cp $(MAXCOMPILERDIR)/lib/MaxCompiler.jar:$(DFESNIPPETS)/src -1.6 -d .`

You should then be able to import any class using the fully qualified name. For example

```
// A Maxeler DFE kernel

import com.custom_computing_ic.dfe_snippets.blas.DenseBlasLib; 

class MyKernel extends Kernel {...}
```

### CPU Libraries

We also provide a number of utilities for CPU code. These are header only libraries, so no compilation is required.
To use them simply add the `include/` directory to your include path.

If you use the Makefile.common provided with `dfe-snippets` you can just add this path to the `CFLAGS` variable. For example: `CFLAGS=(....) -I<path/to/dfesnippets>/include`. You should then be able to use functions, for example:

```
#include <dfesnippets/blas/Blas.hpp>

int main() {
  dfesnippets::blas::Matrix m(10);
  // ....
  return 0;
}
```

## Creating new projects

Use `create.py -h` for a list of available options.

To create and run a __new dfe-snippet__ from the snippets directory run:
```
python scripts/create.py <ProjectName> -c <ProjectConcept>
```

Test this project with:
```
cd <ProjectConcept>/<ProjectName>/build && make runsim
```

To create an external, __stand-alone project__ run:

```
python scripts/create.py <ProjectName>
```

Test this project with:
```
cd <ProjectName>/build && make runsim
```

__Note__ If you select `c++` as the project language, the generated Makefile expects a compiler which supports C++ 11 (i.e the `--std=c++11` command line argument).
