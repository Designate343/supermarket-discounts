# Notes

Please add here any notes, assumptions and design decisions that might help up understand your though process.

- Bumped to java11 and included mockito in the pom to make it easier to test my DiscountService
- Assumed basket contents likely to be small. Current implementation would mean for each discount a foor loop of the whole basket which 
would become unperformant with a large basket and many discount i.e. n*d time (n=basket,d=discounts). Could refactor to 
  looping though basked first as likely the bigger list
  
- Went for a simple working implementation rather than something perfect, should be easier to improve from a working base
than half way through a 'perfect' one
  
Todos

- some way for the discounts to know which products to be applied to
- refactor the discount code to be less repetitive (comment in code about it)