package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.ConnectionFactory;
import model.Contato;

public class ContatoDao {
	private Connection connection;
	
	public ContatoDao() throws SQLException {
		this.connection = ConnectionFactory.getConnection();
	}
	
	public void addContato(Contato contato) throws SQLException {
		String sqlString = "INSERT INTO contatos (nome, endereco, email) VALUES (?, ?, ?)";
		PreparedStatement statement =  connection.prepareStatement(sqlString);
		
		statement.setString(1, contato.getNome());
		statement.setString(2, contato.getEndereco());
		statement.setString(3, contato.getEmail());
		statement.execute();
		statement.close();
		connection.close();
		
	}
	
	public boolean deleteContato(Long id) throws SQLException {
		Contato contato = this.getContatoById(id);
		if (contato != null) {
			String sqlString = "DELETE FROM contatos WHERE id=(?)";
			PreparedStatement statement =  connection.prepareStatement(sqlString);
			
			statement.setLong(1, id);
			statement.execute();
			statement.close();
			connection.close();
			return true;
		}else {
			return false;
		}
		
	}
	
	public List<Contato> getAllContatos() throws SQLException {
		String sqlString = "SELECT * FROM contatos";
		PreparedStatement statement =  connection.prepareStatement(sqlString);
		ResultSet resultSet = statement.executeQuery();
		
		List<Contato> contatos = new ArrayList<Contato>();
		
		while(resultSet.next()) {
			Contato contato = new Contato();
			contato.setId(resultSet.getLong("id"));
			contato.setNome(resultSet.getString("nome"));
			contato.setEmail(resultSet.getString("email"));
			contato.setEndereco(resultSet.getString("endereco"));
			contatos.add(contato);
		}
		resultSet.close();
		statement.close();
		connection.close();
		return contatos;
	
	}
	
	public List<Contato> getContatosByFirstLetter(String letra) throws SQLException {
        List<Contato> contatos = new ArrayList<>();

        String sqlString = "SELECT * FROM contatos WHERE nome LIKE ?";
        PreparedStatement statement = connection.prepareStatement(sqlString);
        statement.setString(1, letra + "%");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Contato contato = new Contato();
            contato.setId(resultSet.getLong("id"));
            contato.setNome(resultSet.getString("nome"));
            contato.setEmail(resultSet.getString("email"));
            contato.setEndereco(resultSet.getString("endereco"));

            contatos.add(contato);
        }

        resultSet.close();
        statement.close();
        connection.close();
        return contatos;
    }
	
	public Contato getContatoById(long id) throws SQLException {
	    String sqlString = "SELECT * FROM contatos WHERE id = ?";
	    PreparedStatement statement = connection.prepareStatement(sqlString);
	    statement.setLong(1, id);
	    ResultSet resultSet = statement.executeQuery();
	    
	    Contato contato = null;
	    
	    if (resultSet.next()) {
	        String nome = resultSet.getString("nome");
	        String endereco = resultSet.getString("endereco");
	        String email = resultSet.getString("email");
	        
	        contato = new Contato();
	        contato.setId(id); // Definir o ID recuperado do banco de dados no objeto Contato
	        contato.setNome(nome);
	        contato.setEndereco(endereco);
	        contato.setEmail(email);
	    }
	    resultSet.close();
	    statement.close();
	    connection.close();
	    return contato;
	}
	
	public boolean updateContato(Contato contato) throws SQLException {
	    String sqlString = "UPDATE contatos SET nome = ?, endereco = ?, email = ? WHERE id = ?";
	    PreparedStatement statement = null;
	    
   
        statement = connection.prepareStatement(sqlString);
        statement.setString(1, contato.getNome());
        statement.setString(2, contato.getEndereco());
        statement.setString(3, contato.getEmail());
        statement.setLong(4, contato.getId());
        statement.executeUpdate();
    
    	Contato contatoBanco = this.getContatoById(contato.getId());
        if (!contatoBanco.equals(contato)) {
        	return false;
            
        }
        statement.close();
        connection.close();
	    return true;
	}
}
