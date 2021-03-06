#!/bin/bash

# Runs a binary in simluation
# Usage:   bash run-sim.sh path/to/binary
# Example: bash run-sim.sh build/bin/fpgaNaive_sim

# DEVICENUM=MAX3424A
DEVICENUM=MAIA
NUMDEVICES=1
SLIC_CONF="default_engine_resource = 192.168.0.10"
MAXELEROSDIR_SIM=${MAXCOMPILERDIR}/lib/maxeleros-sim
MAXOS_SIM=${MAXELEROSDIR_SIM}/lib/libmaxeleros.so

prj=$1

maxcompilersim -n ${USER}a -c${DEVICENUM} -d${NUMDEVICES} restart
# set -o xtrace
expressionFile=$(readlink -f expressions.in)
marketFile=$(readlink -f market.in)
# SLIC_CONF+="use_simulation=${USER}a" LD_PRELOAD=${MAXOS_SIM} ${prj} ${USER}a0:${USER}a $@ ${expressionFile} ${marketFile}

SLIC_CONF+="use_simulation=${USER}a" LD_PRELOAD=${MAXOS_SIM} ${prj} ${USER}a0:${USER}a $2 $3
# set +o xtrace
# maxcompilersim -n ${USER}a -c${DEVICENUM} stop

