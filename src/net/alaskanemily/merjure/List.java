package net.alaskanemily.merjure;

import jmercury.list;
import clojure.lang.Seqable;
import clojure.lang.IPersistentCollection;
import clojure.lang.ISeq;

// Implements Clojure's ISeq using a Mercury list. This lets you use Clojure
// list comprehension on Mercury lists.
public class List<E> implements ISeq {
    
    final private list.List_1<E> m_list;
    final private int m_size; // Minor optimization, we can store the current size here.
    
    // Used in the constructors to get the size of existing lists.
    // All other operations change this by a known amount.
    static private int calculateSize(list.List_1<E> list){
        int size = 0;
        list.List_1<E> counting_list = list;
        while(!list.is_empty(counting_list)){
            size++;
            counting_list = list.det_tail(counting_list);
        }
        return size;
    }
    
    private List(list.List_1<E> other_list, int initial_size){
        m_list = other_list;
        m_size = initial_size;
    }
    
    public List(){
        m_list = null
        m_size = 0;
    }
    
    public List(list.List_1<E> other_list){
        m_list = other_list;
        m_size = calculateSize(other_list);
    }
    
    public ISeq seq() { return new List<E>(); }
    
    public IPersistentCollection empty() { return new List<E>(); }
    
    public Object first(){
        if(m_size == 0)
            return null;
        else
            return list.det_head(m_list);
    }
    
    public ISeq more(){
        if(m_size == 0)
            return new List<E>();
        else if(m_size == 1)
            return new List<E>();
        else
            return new List<E>(list.det_tail(m_list), m_size-1);
    }
    
    // TODO: I'm unsure if returning null on empty is correct.
    public ISeq next(){
        if(m_size < 1)
            return null;
        else
            return new List<E>(list.det_tail(m_list), m_size-1);
    }
    
    public ISeq cons(Object o){
        if(m_size == 0)
            return new List<E>(list.cons((E)o, list.empty_list()), 1);
        else
            return new List<E>(list.cons((E)o, m_list), m_size+1);
    }
    
    public int count() { return m_size; }
    
    public boolean equiv(Object other_sequence_o){
        try{
            
            ISeq other_sequence = (ISeq)other_sequence_o;
            
            if(m_size != other.count())
                return false;
            
            list.List_1<E> counting_list = m_list;
            
            while(!list.is_empty(counting_list)){
                
                final E object = list.det_head(counting_list);
                final E other_object = (E)other_sequence.first();
                
                if(!object.equals(other_object)){
                    return false;
                }
                
                other_sequence = other_sequence.next();
                counting_list = list.det_tail(counting_list);
            }
            return other_sequence == null;
        }
        catch(Exception e){
            return false;
        }
    }
}
