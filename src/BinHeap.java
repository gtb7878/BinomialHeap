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

        // zum Rausschieben
        private boolean negInf = false;

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

    //private  int size;
    private Node<P, D> head;


    // Leere Halde erzeugen.
    BinHeap()
    {
        head = null;
        //size = 0;
    }


    // Ist die Halde momentan leer?
    boolean isEmpty()
    {
        return size() == 0;
    }


    // Größe der Halde, d. h. Anzahl momentan gespeicherter Einträge liefern.

    public int size()
    {
        Node<P, D> next = head;
        int size = 0;
        // Über Wurzeln Knoten aller Bäume addieren
        while (next != null)
        {
            // siehe Folie 91 -> 2^k Knoten
            size += 1 << next.degree;
            next = next.sibling;
        }
        return size;
    }
    /*int size()
    {
        return size;
    }*/


    // Enthält die Halde den Eintrag e?
    boolean contains(Entry<P, D> e)
    {
        if (e == null) return false;
        // zu Wurzelknoten von e
        Node<P, D> par = e.node;
        while (par.parent != null) par = par.parent;

        // Überprüfen, ob Wurzelknoten mit einem Wurzelknoten dieser Halde gleich ist
        Node<P, D> cur = head;

        while(cur != null)
        {
            if (cur == par) return true;
            cur = cur.sibling;
        }
        return false;
    }


    // Neuen Eintrag mit Priorität p und zusätzlichen Daten d erzeugen,
    // zur Halde hinzufügen und zurückliefern. (siehe Seite 97)
    Entry<P, D> insert (P p, D d)
    {
        if (p == null || d == null) return null;

        // Temporäre Halde mit einem Knoten mit Grad 0
        BinHeap<P, D> temp = new BinHeap<>();
        Entry<P, D> e = new Entry<P, D>(p, d);
        Node<P, D> n = new Node<P, D>(e);
        n.degree = 0;
        temp.head = n;
        //temp.size = 1;

        // vereinigen
        BinHeap<P, D> tempHead = mergeHeaps(this, temp);
        head = tempHead.head;
        //size = tempHead.size;

        //size++;
        return e;
    }


    // Priorität des Eintrags e auf p verändern (siehe Folie 98).
    // (Dabei darf auf keinen Fall ein neuer Eintrag entstehen, selbst wenn
    // die Operation intern als Entfernen und Neu-Einfügen implementiert wird!)
    boolean changePrio(Entry<P, D> e, P p)
    {
        if (e == null || p == null || !contains(e)) return false;
        // wenn löschen, zu löschender Knoten als head
        if (e.negInf)
        {
            while (e.node.parent != null)
            {
                e.node.entry = e.node.parent.entry;
                e.node.parent.entry = e;
                e.node.entry.node = e.node;

                e.node = e.node.parent;
            }
            return true;
        }

        // normaler Knoten
        else
        {
            // 1
            if (p.compareTo(e.prio) <= 0)
            {
                // 1.1
                e.prio = p;

                // 1.2
                while (e.node.parent != null)
                {
                    if (e.prio.compareTo(e.node.parent.entry.prio) < 0)
                    {
                        e.node.entry = e.node.parent.entry;
                        e.node.entry.node = e.node;
                        e.node.parent.entry = e;

                        e.node = e.node.parent;
                    }
                    else break;


                }
            }
            // 2
            else
            {
                if (e.node.child != null)
                {
                    remove(e);
                    e.prio = p;
                    insert(e.prio, e.data);
                }
                else e.prio = p;

            }
        }
        return true;
    }



    // Einen Eintrag mit minimaler Priorität liefern (siehe Folie 97).
    Entry<P, D> minimum()
    {
        if (head == null) return null;

        Node<P, D> next = head, min = head;
        // in Wurzelliste Objekt mit minimaler Priorität finden
        while (next != null)
        {
            if (next.entry.negInf)
            {
                return next.entry;
            }
            if (min.entry.prio.compareTo(next.entry.prio) > 0) min = next;
            next = next.sibling;
        }
        return min.entry;
    }


    // Einen Eintrag mit minimaler Priorität liefern und aus der Halde entfernen (siehe Folie 97).
    Entry<P, D> extractMin()
    {
        // 1
        // suchen
        Entry<P, D> min = minimum();
        if (min == null) return null;

        // entfernen
        if (min.node == head)
        {
            head = head.sibling;
        }
        else
        {
            Node<P, D> next = head;
            while (next.sibling != min.node) next = next.sibling;
            next.sibling = min.node.sibling;
        }

        // 2

        // neue Halde mit Siblings erstellen und vereinen
        if (min.node.child != null)
        {
            BinHeap<P, D> h2 = new BinHeap<>();
            h2.head = min.node.child.sibling;

            h2.head.parent = null;
            Node<P, D> next = h2.head.sibling;
            next.parent = null;
            //Node<P, D> hSave = next;
            while (next.sibling != h2.head)
            {
                next.parent = null;
                next = next.sibling;
            }
            next.parent = null;
            next.sibling = null;
            //h2.head = hSave;



            BinHeap<P,D> temp = mergeHeaps(this, h2);
            /*next = temp.head;
            if (next.sibling != null){
                while (next.sibling.sibling != null && next.sibling != next.sibling.sibling)
                {
                    next.parent = null;
                    next = next.sibling;
                }
            }*/

            head = temp.head;
            //size = temp.size;
        }

        //size--;
        return min;
    }


    // Eintrag e aus der Halde entfernen (siehe Folie 98).
    boolean remove(Entry<P, D> e)
    {
        if (e == null || !contains(e)) return false;
        // 1
        e.negInf = true;
        changePrio(e, e.prio);
        // 2
        extractMin();
        return true;
    }


    // Inhalt der Halde zu Testzwecken ausgeben.
    public void dump()
    {

        if(head != null)
        {
            dump(head, head, 0);
            /*Node<P, D> next = head.sibling;
            while (next != null)
            {
                dump(next, head, 0);
                next = next.sibling;
            }*/

        }

        //pSpace = 0;
    }




    // Hilfsmethoden


    // Hilfsmethode für dump
    private void dump(Node<P, D> cur,Node<P, D>  start, int pSpace)
    {
        //Node<P, D> cur = n;


        for(int i = 0; i < pSpace; i++)
        {
            System.out.print("  ");
        }

        System.out.print(cur.entry.prio().toString() + " ");
        System.out.println(cur.entry.data().toString());


        if (cur.child != null) dump(cur.child.sibling, cur.child.sibling, pSpace + 1);
        if (cur.sibling == null) return;
        if (cur.sibling == start) return;
        dump(cur.sibling, start, pSpace);


        //else if(cur.parent != null && cur.parent.sibling != null)
        //{
        //    if( cur.parent != cur.parent.sibling) dump(cur.parent.sibling, pSpace - 1);
        //}

    }


    // Zusammenfassen zweier Bäume B1 und B2 mit Grad k
    // zu einem Baum mit Grad k+1 (siehe Folie 95).
    private Node<P, D> mergeTrees(Node<P, D> b1, Node<P, D> b2)
    {
        if (b1 == null || b2 == null) return null;

        // 1
        if (b1.entry.prio.compareTo(b2.entry.prio) > 0)
        {

            b2.sibling = null;
            b2.degree = b2.degree + 1;
            b1.parent = b2;
            if (b2.child == null) b2.child = b1.sibling = b1;
            else
            {
                b1.sibling = b2.child.sibling;
                b2.child = b2.child.sibling = b1;
            }
            return b2;
        }

        // 2
        else
        {
            b1.sibling = null;
            b1.degree = b1.degree + 1;
            b2.parent = b1;
            if (b1.child == null) b1.child = b2.sibling = b2;
            else
            {
                b2.sibling = b1.child.sibling;
                b1.child = b1.child.sibling = b2;
            }
            return b1;
        }
    }


    // Vereinigen zweier Halden H1 und H2 zu einer neuen Halde H (siehe Folie 96).
    private BinHeap<P, D> mergeHeaps(BinHeap<P, D> h1, BinHeap<P, D> h2)
    {
        BinHeap<P, D> h = new BinHeap<>();
        //h.size = h1.size;
        if (h1 == null || h2 == null) return null;

        // 1
        Node<P, D>[] trees = new Node[3];

        // 2
        int k = 0;

        // 3
        while (!h1.isEmpty() || !h2.isEmpty() || !(trees[0] == null && trees[1] == null && trees[2] == null))
        {
            // 3.1
            if (h1.head != null)
            {
                if (h1.head.degree == k)
                {
                    for (int i = 0; i < 3; i++)
                    {
                        if (trees[i] == null)
                        {
                            trees[i] = h1.head;

                            break;
                        }
                    }
                    //h1.size -= Math.pow(2, h1.head.degree);
                    h1.head = h1.head.sibling;

                    //h.size++;
                }
            }
            // 3.2
            if (h2.head != null)
            {
                if (h2.head.degree == k)
                {
                    for (int i = 0; i < 3; i++)
                    {
                        if (trees[i] == null)
                        {
                            trees[i] = h2.head;
                            break;
                        }
                    }
                    //h2.size -= Math.pow(2, h2.head.degree);
                    h2.head = h2.head.sibling;

                    //h.size++;
                }
            }
            // 3.3

            int count = 0;
            for (int i = 0; i < 3; i++)
            {
                if (trees[i] != null)
                {
                    count++;
                }
            }

            if (count == 1 || count == 3)
            {
                Node<P, D> take = null;
                for (int i = 0; i < 3; i++)
                {
                    if (trees[i] != null)
                    {
                        take = trees[i];
                        take.sibling = null;
                        trees[i] = null;
                        break;
                    }
                }

                if (h.head == null) h.head = take;
                else
                {
                    Node<P, D> skipSib = h.head;
                    while (skipSib.sibling != null) skipSib = skipSib.sibling;
                    skipSib.sibling = take;
                    //break; // test
                }
            }

            // 3.4
            if (count != 2)
            {
                count = 0;
                for (int i = 0; i < 3; i++)
                {
                    if (trees[i] != null)
                    {
                        count++;
                    }
                }
            }

            if (count == 2)
            {
                Node<P, D> tree1 = null, tree2 = null;
                for (int i = 0; i < 3; i++)
                {
                    if (trees[i] != null)
                    {
                        tree1 = trees[i];
                        trees[i] = null;
                        break;
                    }
                }
                for (int i = 0; i < 3; i++)
                {
                    if (trees[i] != null)
                    {
                        tree2 = trees[i];
                        trees[i] = null;
                        break;
                    }
                }

                Node<P, D> mTree = mergeTrees(tree1, tree2);
                for (int i = 0; i < 3; i++)
                {
                    if (trees[i] == null)
                    {
                        trees[i] = mTree;
                        break;
                    }
                }

            }

            // 3.5
            k++;

        }
        //h.size++;
        return h;
    }
}