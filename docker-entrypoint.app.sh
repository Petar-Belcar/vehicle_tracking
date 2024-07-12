#!/bin/sh

java -cp jar_file.jar edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.posluzitelji.CentralniSustav NWTiS_DZ1_CS.txt &

java -cp jar_file.jar edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.posluzitelji.PosluziteljKazni NWTiS_DZ1_PK.txt

wait
