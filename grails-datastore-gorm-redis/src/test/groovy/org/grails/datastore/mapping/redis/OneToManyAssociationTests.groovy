package org.grails.datastore.mapping.redis

import grails.persistence.Entity

import org.junit.Test
import org.grails.datastore.mapping.core.Session

/**
 * @author Graeme Rocher
 * @since 1.0
 */
class OneToManyAssociationTests extends AbstractRedisTest {

    @Test
    void testOneToManyAssociation() {
        ds.mappingContext.addPersistentEntity(Author)

        def a = new Author(name:"Stephen King")
        a.books = [ new Book(title:"The Stand"), new Book(title:"It")]

        session.persist(a)

        a = session.retrieve(Author, a.id)

        assert a != null
        assert "Stephen King" == a.name
        assert a.books != null
        assert 2 == a.books.size()

        def b1 = a.books.find { it.title == 'The Stand'}
        assert b1 != null
        assert b1.id != null
        assert "The Stand" == b1.title
    }
}

@Entity
class Author {
    Long id
    String name
    List books
    static hasMany = [books:Book]
}

@Entity
class Book {
    Long id
    String title

    static mapping = {
        title index:true
    }
}
