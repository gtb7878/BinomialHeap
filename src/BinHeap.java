// Als Binomial-Halde implementierte Minimum-Vorrangwarteschlange
// mit Prioritäten eines beliebigen Typs P (der die Schnittstelle
// Comparable<P> oder Comparable<P'> für einen Obertyp P' von P
// implementieren muss) und zusätzlichen Daten eines beliebigen Typs D.
class BinHeap <P extends Comparable<? super P>, D> {
    // Eintrag einer solchen Warteschlange bzw. Halde, bestehend aus
    // einer Priorität prio mit Typ P und zusätzlichen Daten data mit
    // Typ D.
    // Wenn der Eintrag momentan tatsächlich zu einer Halde gehört,
    // verweist node auf den zugehörigen Knoten eines Binomialbaums
    // dieser Halde.
    public static class Entry <P, D> {
        // Priorität, zusätzliche Daten und zugehöriger Knoten.
        private P prio;
        private D data;
        private Node<P, D> node;

        // Eintrag mit Priorität p und zusätzlichen Daten d erzeugen.
        private Entry (P p, D d) {
            prio = p;
            data = d;
        }

        // Priorität bzw. zusätzliche Daten liefern.
        public P prio () { return prio; }
        public D data () { return data; }
    }

    // Knoten eines Binomialbaums innerhalb einer solchen Halde.
    // Neben den eigentlichen Knotendaten (degree, parent, child,
    // sibling), enthält der Knoten einen Verweis auf den zugehörigen
    // Eintrag.
    private static class Node <P, D> {
        // Zugehöriger Eintrag.
        private Entry<P, D> entry;

        // Grad des Knotens.
        private int degree;

        // Vorgänger (falls vorhanden; bei einem Wurzelknoten null).
        private Node<P, D> parent;

        // Nachfolger mit dem größten Grad
        // (falls vorhanden; bei einem Blattknoten null).
        private Node<P, D> child;

        // Zirkuläre Verkettung aller Nachfolger eines Knotens
        // bzw. einfache Verkettung aller Wurzelknoten einer Halde,
        // jeweils sortiert nach aufsteigendem Grad.
        private Node<P, D> sibling;

        // Knoten erzeugen, der auf den Eintrag e verweist
        // und umgekehrt.
        private Node (Entry<P, D> e) {
            entry = e;
            e.node = this;
        }

        // Priorität des Knotens, d. h. des zugehörigen Eintrags
        // liefern.
        private P prio () { return entry.prio; }
    }


    // Code

    private int size;
    private Node<P, D> head;


    // Leere Halde erzeugen.
    BinHeap()
    {
        head = null;
    }


    // Ist die Halde momentan leer?
    boolean isEmpty()
    {
        return size() == 0;
    }


    // Größe der Halde, d. h. Anzahl momentan gespeicherter Einträge liefern.
    int size()
    {
        return size;
    }


    // Enthält die Halde den Eintrag e?
    boolean contains(Entry<P, D> e)
    {
        return false;
    }


    // Neuen Eintrag mit Priorität p und zusätzlichen Daten d erzeugen,
    // zur Halde hinzufügen und zurückliefern.
    Entry<P, D> insert (P p, D d)
    {
        return null;
    }


    // Priorität des Eintrags e auf p verändern.
    // (Dabei darf auf keinen Fall ein neuer Eintrag entstehen, selbst wenn
    // die Operation intern als Entfernen und Neu-Einfügen implementiert wird!)
    boolean changePrio(Entry<P, D> e, P p)
    {
        return false;
    }


    // Einen Eintrag mit minimaler Priorität liefern.
    Entry<P, D> minimum()
    {
        return null;
    }


    // Einen Eintrag mit minimaler Priorität liefern und aus der Halde entfernen.
    Entry<P, D> extractMin()
    {
        return null;
    }


    // Eintrag e aus der Halde entfernen.
    boolean remove(Entry<P, D> e)
    {
        return false;
    }


    // Inhalt der Halde zu Testzwecken ausgeben.
    void dump()
    {

    }


    // Hilfsmethoden


    // Zusammenfassen zweier Bäume B1 und B2 mit Grad k
    // zu einem Baum mit Grad k+1
    private Node<P, D> mergeTrees(Node<P, D> root1, Node<P, D> root2)
    {
        return null;
    }


    // Vereinigen zweier Halden H1 und H2 zu einer neuen Halde H.
    private BinHeap<P, D> mergeHeaps(BinHeap<P, D> heap1, BinHeap<P, D> heap2)
    {
        return null;
    }


}