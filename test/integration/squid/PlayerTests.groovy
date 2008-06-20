package squid

class PlayerTests extends GroovyTestCase {

    void testPlayerAdded() {
        def game = new Game(10, 10, 'A', 'B')
        game.save(flush: true)
        assertEquals(Player.list().size(), 2)
        assertEquals(Player.findByName('A').name, 'A')
    }
}
