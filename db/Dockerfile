
FROM mysql:8.0.40

ENV MYSQL_ROOT_PASSWORD=example

COPY test_db/ /docker-entrypoint-initdb.d/test_db/

RUN printf '#!/bin/bash\ncd /docker-entrypoint-initdb.d/test_db\nmysql -uroot -p"$MYSQL_ROOT_PASSWORD" < employees.sql\n' > /docker-entrypoint-initdb.d/00-init.sh \
    && chmod +x /docker-entrypoint-initdb.d/00-init.sh
