FROM mysql

# Set the root password
ENV MYSQL_ROOT_PASSWORD=example

# Copy the test_db folder into the entrypoint initialization directory
COPY test_db/ /docker-entrypoint-initdb.d/test_db/

# Create an initialization script that enables client commands and pipes employees.sql
RUN echo '#!/bin/bash' > /docker-entrypoint-initdb.d/00-init.sh && \
    echo 'cd /docker-entrypoint-initdb.d/test_db' >> /docker-entrypoint-initdb.d/00-init.sh && \
    echo 'mysql --commands=1 -uroot -p"$MYSQL_ROOT_PASSWORD" < employees.sql' >> /docker-entrypoint-initdb.d/00-init.sh && \
    chmod +x /docker-entrypoint-initdb.d/00-init.sh