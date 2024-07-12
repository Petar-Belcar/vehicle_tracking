FROM amazoncorretto:22-jdk AS builder

FROM amazoncorretto:22-jdk

# Za debugiranje
# EXPOSE 8000 8001 8020

WORKDIR /app

COPY pbelcar_vjezba_08_dz_3_app/target/pbelcar_vjezba_08_dz_3_app-1.1.0-jar-with-dependencies.jar /app/jar_file.jar
COPY pbelcar_vjezba_08_dz_3_app/NWTiS_DZ1_CS.txt /app/NWTiS_DZ1_CS.txt 
COPY pbelcar_vjezba_08_dz_3_app/NWTiS_DZ1_PK.txt /app/NWTiS_DZ1_PK.txt 
COPY docker-entrypoint.app.sh /app/script.sh

RUN chmod 777 /app/script.sh

ENTRYPOINT ["/app/script.sh"]
