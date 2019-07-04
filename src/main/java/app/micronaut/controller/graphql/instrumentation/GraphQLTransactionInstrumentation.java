package app.micronaut.controller.graphql.instrumentation;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecuteOperationParameters;
import graphql.language.OperationDefinition.Operation;
import io.micronaut.context.annotation.Requires;

/**
 * Opens and closes the transaction before and after GraphQL query execution.
 */
@Singleton
@Requires(beans = PlatformTransactionManager.class)
public class GraphQLTransactionInstrumentation extends SimpleInstrumentation {

    public static final Logger logger = LoggerFactory.getLogger(GraphQLTransactionInstrumentation.class);
    public static final String TRANSACTION_NAME = "graphQLTransaction";

    @Inject
    protected PlatformTransactionManager tm;

    @Override
    public InstrumentationContext<ExecutionResult> beginExecuteOperation(InstrumentationExecuteOperationParameters parameters) {

        logger.debug("Begining transaction for GraphQL execution: {}", Thread.currentThread().getName());

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName(TRANSACTION_NAME);
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setReadOnly(!Operation.MUTATION.equals(parameters.getExecutionContext().getOperationDefinition().getOperation()));
        TransactionStatus status = tm.getTransaction(def);

        return SimpleInstrumentationContext.whenCompleted((t, e) -> {
            if (status.isRollbackOnly() || e != null) {
                tm.rollback(status);
                logger.debug("Rolling back transaction for GraphQL execution: {}", Thread.currentThread().getName());
            } else {
                tm.commit(status);
                logger.debug("Commiting transaction for GraphQL execution: {}", Thread.currentThread().getName());
            }
        });

    }

}