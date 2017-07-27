package fr.lteconsulting.dao;

import java.sql.Connection;

public interface InTransactionExecution<T>
{
	T execute( Connection connection ) throws Exception;
}
