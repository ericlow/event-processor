
This was a fun little coding exercise that took me a bit of time for nights and weekends. I need to move on now, but I've
had fun working on this.  There are some issues with the code which I will write down now while it is still fresh in my mind.


### Timestamps

One of the key assumptions and shortcuts in my system is that I assume that each record will come in a one second interval.
If, in the real world, this assumption would be broken, then significant changes would have to happen across the algorithms 
and records.  There would probably be a great deal more conversation with the PM about corner cases as well. 

I think in the real world, we would want to go in with this assumption of 1 sec per currency pair record, launch our POC and
see how far we get.  If we find that we have a good business result, we would probably want to go back and consider what changes
to make for other intervals of data, or based on PM priorities, take on other priorities.

For the sake of getting this back to you, taking the assumption that data arrives once per second simplifies the coding, and 
I gladly take this shortcut.



### What are the responsibilities of each Service? 
I struggled with this some.  Should the CurrencyConversionRateHistory maintain moving average or should that be data that 
lives with the AlertService?  I think at this point, the app is small, so mistakes are generally not small.  But as we build the
whole application out, the answer may become clear at some point. I leaned towards making moving average part of the 
CurrencyConversionRateHistory class/service because a 5 minute moving average seems like it might be helpful for other clients
at a later time.

To me it seems that the expandability of this service is almost certainly in creating different types of alerts based off the
currency stream / history.  So it seems that building easy ways to expand the different types of alerts is practically a
requirement.  For this, see the AlertServiceFactory, which, for now, will be the centralized location to create the list of 
alerts and configure them.  Since they are all in different classes, I feel this design follows SOLID design principles well. 

For me, I believe responsibilities as well as interface design are some of the key design decisions that you make in a system. 
Paired with high readability code, are the focus of my code reviews and the added value that I can provide.

The EventProcessor is more of a coordinator / broker / delegator more than everything else.  I don't see it having a great deal of responsibility.


### The AlertService family
I think a key change to me was to recognize that each alert service could be currency pair specific, this simplified the code somewhat
but made it a great deal more modular that my original design which was an AlertService for all types of alerts and currency pairs.  I
am happy with this choice.


## Testing
I've added unit tests as well as created a few test input files to test the operation of the code