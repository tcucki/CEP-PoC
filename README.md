# Complex Event Processing Proof of Concept project.

This project aims to try CEP approaches using Apache Kafka and confluent KSQL.

## Requirements:
- Zookeeper, Kafka, schema registry, ksql-cli

Follow https://github.com/confluentinc/ksql/blob/master/docs/quickstart/quickstart-docker.md#docker-setup-for-ksql to setup requirements from Docker image. 

On **ksql-cli** utility, run the following commands to setup stream and tables:

Creates **tx_stream** from **transaction** topic:
```
create stream tx_stream (\
id bigint,\
time bigint,\
origin int,\
transactiontype varchar,\
instrumenttype varchar,\
amount double,\
strunmentid bigint) \
with (kafka_topic='transaction', value_format='JSON');
```

Creates table *tx_count* from *tx_stream* throwing events when a card is unloaded more than 10 times on 15 seconds:
```
create table tx_count as \
select instrumentid, count(*) purchasecount \
from tx_stream \
window tumbling (size 15 second) \
where transactiontype='UNLOAD' and instrumenttype='CARD' \
group by instrumentid \
having count(*) > 10;
```

Creates table *tx_sum* from *tx_stream* throwing events when the cumulative amount unloaded from a card is greater than 500 on 15 seconds:
```
create table tx_sum as \
select instrumentid, sum(amount) purchasesum \
from tx_stream \
window tumbling (size 15 second) \
where transactiontype='UNLOAD' and instrumenttype='CARD' \
group by instrumentid \
having sum(amount) > 500;
```

## cep-poc-commons

Library for common functionality.

## ceppoctransaction

### cep-poc-producer

Creates and publishes transactions for topic `transaction`

### cep-poc-consumer

Just consumes events from `transaction` topic

### cep-poc-monitor

Consumes events from table topics `tx_count` and `tx_sum`
