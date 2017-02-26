package com.example.will.chalten;

import com.squareup.otto.Bus;

/**
 * Created by brianoleary on 2/26/17.
 */

public class BusProvider {


        private static final Bus BUS = new Bus();

        public static Bus getInstance(){
            return BUS;
        }

        public BusProvider(){}

}
